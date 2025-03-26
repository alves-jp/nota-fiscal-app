import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InvoiceItem, InvoiceItemDTO } from '../../../../core/models/invoice-item.model';
import { Product } from '../../../../core/models/product.model';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'app-invoice-item-form',
  templateUrl: './invoice-item-form.component.html',
  styleUrls: ['./invoice-item-form.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    InputNumberModule,
    ButtonModule,
    ToastModule,
    InputTextModule
  ],
  providers: [MessageService]
})
export class InvoiceItemFormComponent implements OnInit {
  @Input() invoiceItem?: InvoiceItem;
  @Input() invoiceId!: number;
  @Input() products: Product[] = [];
  @Output() formSubmit = new EventEmitter<InvoiceItemDTO>();
  @Output() formCancel = new EventEmitter<void>();

  invoiceItemForm: FormGroup;
  filteredProducts: Product[] = [];
  searchQuery: string = '';
  showSuggestions: boolean = false;
  selectedProduct: Product | null = null;

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService
  ) {
    this.invoiceItemForm = this.fb.group({
      id: [null],
      product: [null, Validators.required],
      unitPrice: [null, [Validators.required, Validators.min(0.01)]],
      quantity: [1, [Validators.required, Validators.min(1)]],
      total: [{value: 0, disabled: true}]
    });
  }

  ngOnInit(): void {
    if (this.invoiceItem) {
      this.invoiceItemForm.patchValue({
        id: this.invoiceItem.id,
        product: this.invoiceItem.product,
        unitPrice: this.invoiceItem.unitPrice,
        quantity: this.invoiceItem.quantity,
        total: this.invoiceItem.unitPrice * this.invoiceItem.quantity
      });
      this.selectedProduct = this.invoiceItem.product;
      this.searchQuery = this.invoiceItem.product ? 
        `${this.invoiceItem.product.productCode} - ${this.invoiceItem.product.description}` : '';
    }
    
    this.invoiceItemForm.get('unitPrice')?.valueChanges.subscribe(() => this.updateTotal());
    this.invoiceItemForm.get('quantity')?.valueChanges.subscribe(() => this.updateTotal());
  }

  onProductSearchInput(event: Event): void {
    const query = (event.target as HTMLInputElement).value;
    this.searchQuery = query;
    this.showSuggestions = query.length > 0;
    
    if (!this.showSuggestions) {
      this.filteredProducts = [];
      return;
    }
    
    const searchTerm = query.toLowerCase();
    this.filteredProducts = this.products.filter(product => 
      product.description.toLowerCase().includes(searchTerm) || 
      (product.productCode && product.productCode.toLowerCase().includes(searchTerm)) ||
      (product.productStatus && product.productStatus.toLowerCase().includes(searchTerm))
    );
  }

  selectProduct(product: Product): void {
    this.selectedProduct = product;
    this.invoiceItemForm.patchValue({
      product: product
    });
    this.searchQuery = `${product.productCode} - ${product.description}`;
    this.showSuggestions = false;
  }

  updateTotal(): void {
    const unitPrice = this.invoiceItemForm.get('unitPrice')?.value || 0;
    const quantity = this.invoiceItemForm.get('quantity')?.value || 0;
    this.invoiceItemForm.get('total')?.setValue(unitPrice * quantity, {emitEvent: false});
  }

  onSubmit(): void {
    if (this.invoiceItemForm.valid) {
      const formValue = this.invoiceItemForm.value;
      const itemData: InvoiceItemDTO = {
        invoiceId: this.invoiceId,
        productId: formValue.product.id,
        unitPrice: formValue.unitPrice,
        quantity: formValue.quantity
      };
      this.formSubmit.emit(itemData);
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: 'Preencha todos os campos obrigatórios corretamente'
      });
    }
  }

  onCancel(): void {
    this.formCancel.emit();
  }
}