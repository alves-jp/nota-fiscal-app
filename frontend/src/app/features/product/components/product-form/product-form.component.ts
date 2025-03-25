import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Product } from '../../../../core/models/product.model';
import { ProductService } from '../../../../core/services/api/product.service';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputNumberModule } from 'primeng/inputnumber';
import { ReactiveFormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    ToastModule,
    InputNumberModule,
    ReactiveFormsModule,
    DropdownModule
  ]
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
    private productService: ProductService,
    private messageService: MessageService
  ) {
    this.productForm = this.fb.group({
      id: [null],
      code: ['', [Validators.required, Validators.maxLength(20)]],
      name: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', Validators.maxLength(255)],
      price: [null, [Validators.required, Validators.min(0)]],
      status: ['ACTIVE', Validators.required]
    });
  }

  ngOnInit(): void {
    if (this.product) {
      this.productForm.patchValue(this.product);
    }
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      const productData = this.productForm.value;

      if (productData.id) {
        this.productService.updateProduct(productData.id, productData).subscribe({
          next: (updatedProduct) => {
            this.messageService.add({
              severity: 'success', 
              summary: 'Sucesso', 
              detail: 'Produto atualizado'
            });
            this.formSubmit.emit(updatedProduct);
          },
          error: (error) => {
            this.messageService.add({
              severity: 'error', 
              summary: 'Erro', 
              detail: 'Falha ao atualizar produto'
            });
          }
        });
      } else {
        this.productService.createProduct(productData).subscribe({
          next: (newProduct) => {
            this.messageService.add({
              severity: 'success', 
              summary: 'Sucesso', 
              detail: 'Produto criado'
            });
            this.formSubmit.emit(newProduct);
          },
          error: (error) => {
            this.messageService.add({
              severity: 'error', 
              summary: 'Erro', 
              detail: 'Falha ao criar produto'
            });
          }
        });
      }
    }
  }

  onCancel(): void {
    this.formCancel.emit();
  }
}