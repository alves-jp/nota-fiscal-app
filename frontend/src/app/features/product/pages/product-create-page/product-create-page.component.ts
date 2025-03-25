import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductFormComponent } from '../../components/product-form/product-form.component';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { Product } from '../../../../core/models/product.model';
import { ProductService } from '../../../../core/services/api/product.service';

@Component({
  selector: 'app-product-create-page',
  templateUrl: './product-create-page.component.html',
  styleUrls: ['./product-create-page.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ProductFormComponent,
    CardModule,
    ToastModule
  ],
  providers: [MessageService]
})
export class ProductCreatePageComponent {
  loading = false;
  submitted = false;

  constructor(
    private productService: ProductService,
    private router: Router,
    private messageService: MessageService
  ) {}

  handleFormSubmit(product: Product): void {
    this.submitted = true;
    this.loading = true;

    this.productService.createProduct(product).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Produto criado com sucesso',
          life: 3000
        });
        setTimeout(() => this.router.navigate(['/produtos']), 1500);
      },
      error: (error) => {
        this.loading = false;
        const errorMessage = error.error?.message || 'Erro ao criar produto';
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: errorMessage,
          life: 5000
        });
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/produtos']);
  }
}