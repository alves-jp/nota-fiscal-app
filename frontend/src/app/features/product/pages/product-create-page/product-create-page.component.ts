import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextarea } from 'primeng/inputtextarea';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { Product } from '../../../../core/models/product.model';
import { ProductService } from '../../../../core/services/api/product.service';

interface StatusOption {
  label: string;
  value: 'ACTIVE' | 'INACTIVE' | 'PENDING';
}

@Component({
  selector: 'app-product-create-page',
  templateUrl: './product-create-page.component.html',
  styleUrls: ['./product-create-page.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CardModule,
    InputTextModule,
    ButtonModule,
    DropdownModule,
    InputTextarea,
    ToastModule
  ],
  providers: [MessageService]
})
export class ProductCreatePageComponent {
  product: Partial<Product> = {
    productCode: '',
    description: '',
    productStatus: 'ACTIVE'
  };

  submitted = false;
  loading = false;

  statusOptions: StatusOption[] = [
    { label: 'Ativo', value: 'ACTIVE' },
    { label: 'Inativo', value: 'INACTIVE' }
  ];

  constructor(
    private productService: ProductService,
    private router: Router,
    private messageService: MessageService
  ) {}

  onSubmit(): void {
    this.submitted = true;
    
    if (!this.isFormValid()) {
      this.showError('Preencha todos os campos obrigatórios');
      return;
    }

    this.loading = true;
    this.productService.createProduct(this.product as Product).subscribe({
      next: () => this.handleSuccess(),
      error: (err) => this.handleError(err)
    });
  }

  private isFormValid(): boolean {
    return !!this.product.productCode && 
           !!this.product.description && 
           !!this.product.productStatus;
  }

  private handleSuccess(): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Sucesso',
      detail: 'Produto criado com sucesso',
      life: 3000
    });
    setTimeout(() => this.router.navigate(['/produtos']), 1500);
  }

  private handleError(error: any): void {
    this.loading = false;
    const errorMessage = error.error?.message || 'Erro ao criar produto';
    this.showError(errorMessage);
  }

  private showError(detail: string): void {
    this.messageService.add({
      severity: 'error',
      summary: 'Erro',
      detail,
      life: 5000
    });
  }

  onCancel(): void {
    this.router.navigate(['/produtos']);
  }
}