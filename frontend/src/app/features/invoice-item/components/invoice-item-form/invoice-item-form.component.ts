import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InvoiceItem, InvoiceItemDTO } from '../../../../core/models/invoice-item.model';
import { Product } from '../../../../core/models/product.model';
import { ProductService } from '../../../../core/services/api/product.service';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { ReactiveFormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputNumberModule } from 'primeng/inputnumber';

@Component({
  selector: 'app-invoice-item-form',
  templateUrl: './invoice-item-form.component.html',
  styleUrls: ['./invoice-item-form.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    ToastModule,
    FormsModule,
    InputTextModule,
    ReactiveFormsModule,
    DropdownModule,
    ButtonModule,
    InputNumberModule
  ],
  providers: [MessageService]
})
export class InvoiceItemFormComponent implements OnInit {
  @Input() invoiceId!: number;
  @Input() item?: InvoiceItem;
  @Output() formSubmit = new EventEmitter<InvoiceItemDTO>();
  @Output() formCancel = new EventEmitter<void>();

  itemForm: FormGroup;
  filteredProducts: Product[] = [];
  products: Product[] = [];
  searchQuery: string = '';
  showSuggestions: boolean = false;
  selectedProduct: Product | null = null;

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private productService: ProductService
  ) {
    this.itemForm = this.fb.group({
      id: [null],
      productId: [null, Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      unitPrice: [0, [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit(): void {
    this.loadProducts();
    
    if (this.item) {
      this.patchFormWithItemData();
      
      if (this.item.id) {
        const productSearchInput = document.getElementById('productSearch') as HTMLInputElement;
        
        if (productSearchInput) {
          productSearchInput.disabled = true;
        }
      }
    }
  }

  private loadProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products.filter(p => p.productStatus === 'ACTIVE');
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: 'Não foi possível carregar os produtos'
        });
      }
    });
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
      (product.productCode && product.productCode.toLowerCase().includes(searchTerm))
    );
  }

  selectProduct(product: Product): void {
    this.selectedProduct = product;
    this.itemForm.patchValue({
      productId: product.id
    });
    
    this.searchQuery = `${product.productCode} - ${product.description}`;
    this.showSuggestions = false;
  }

  private patchFormWithItemData(): void {
    if (!this.item) return;

    this.itemForm.patchValue({
      id: this.item.id,
      productId: this.item.product.id,
      quantity: this.item.quantity,
      unitPrice: this.item.unitValue
    });

    if (this.item.product) {
      this.selectedProduct = this.item.product;
      this.searchQuery = `${this.item.product.productCode} - ${this.item.product.description}`;
    }
  }

  onSubmit(): void {
    if (this.itemForm.invalid) {
      this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: 'Por favor, preencha todos os campos obrigatórios corretamente'
      });

      this.markAllAsTouched();
      return;
    }

    const formValue = this.itemForm.value;
    const itemData: InvoiceItemDTO = {
      id: this.item?.id,
      invoiceId: this.invoiceId,
      productId: formValue.productId,
      quantity: formValue.quantity,
      unitValue: formValue.unitPrice,
      totalValue: this.calculateTotal()
    };

    this.formSubmit.emit(itemData);
  }

  private markAllAsTouched(): void {
    Object.values(this.itemForm.controls).forEach(control => {
      control.markAsTouched();
    });
  }

  onCancel(): void {
    this.formCancel.emit();
  }

  calculateTotal(): number {
    if (this.itemForm.valid) {
      const quantity = this.itemForm.get('quantity')?.value || 0;
      const unitPrice = this.itemForm.get('unitPrice')?.value || 0;

      return quantity * unitPrice;
    }
    return 0;
  }

  formatCurrency(value: number): string {
    return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
}