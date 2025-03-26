import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InvoiceItem, InvoiceItemDTO } from '../../../../core/models/invoice-item.model';
import { Product } from '../../../../core/models/product.model';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-invoice-item-form',
  templateUrl: './invoice-item-form.component.html',
  styleUrls: ['./invoice-item-form.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    DropdownModule,
    InputNumberModule,
    ButtonModule,
    ToastModule
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

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService
  ) {
    this.invoiceItemForm = this.fb.group({
      id: [null],
      product: [null, Validators.required],
      unitPrice: [null, [Validators.required, Validators.min(0.01)]],
      quantity: [1, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    if (this.invoiceItem) {
      this.invoiceItemForm.patchValue({
        id: this.invoiceItem.id,
        product: this.invoiceItem.product,
        unitPrice: this.invoiceItem.unitPrice,
        quantity: this.invoiceItem.quantity
      });
    }
  }

  calculateTotal(): number {
    const unitPrice = this.invoiceItemForm.get('unitPrice')?.value || 0;
    const quantity = this.invoiceItemForm.get('quantity')?.value || 0;
    return unitPrice * quantity;
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