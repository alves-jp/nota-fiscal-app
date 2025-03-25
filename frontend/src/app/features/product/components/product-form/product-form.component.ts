import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Product } from '../../../../core/models/product.model';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { ReactiveFormsModule } from '@angular/forms';
import { SelectButtonModule } from 'primeng/selectbutton';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    ToastModule,
    InputTextModule,
    ReactiveFormsModule,
    SelectButtonModule,
    ButtonModule
  ],
  providers: [MessageService]
})
export class ProductFormComponent implements OnInit {
  @Input() product?: Product;
  @Output() formSubmit = new EventEmitter<Product>();
  @Output() formCancel = new EventEmitter<void>();

  productForm: FormGroup;
  statusOptions = [
    { label: 'Ativo', value: 'ACTIVE' },
    { label: 'Inativo', value: 'INACTIVE' }
  ];

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService
  ) {
    this.productForm = this.fb.group({
      id: [null],
      code: ['', [Validators.required, Validators.maxLength(20)]],
      name: ['', [Validators.required, Validators.maxLength(255)]],
      status: ['ACTIVE', Validators.required]
    });
  }

  ngOnInit(): void {
    if (this.product) {
      this.productForm.patchValue({
        id: this.product.id,
        code: this.product.productCode,
        name: this.product.description,
        status: this.product.productStatus
      });
    }
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      const formValue = this.productForm.value;
      const productData: Product = {
        id: formValue.id,
        productCode: formValue.code,
        description: formValue.name,
        productStatus: formValue.status
      };
      this.formSubmit.emit(productData);
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: 'Preencha todos os campos obrigatórios'
      });
    }
  }

  onCancel(): void {
    this.formCancel.emit();
  }
}