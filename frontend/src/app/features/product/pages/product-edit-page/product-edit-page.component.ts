import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../../../core/services/api/product.service';
import { Product } from '../../../../core/models/product.model';
import { MessageService } from 'primeng/api';
import { ProductFormComponent } from '../../components/product-form/product-form.component';

@Component({
  selector: 'app-product-edit-page',
  templateUrl: './product-edit-page.component.html',
  styleUrls: ['./product-edit-page.component.scss'],
  standalone: true,
  imports: [CommonModule, ProductFormComponent]
})
export class ProductEditPageComponent implements OnInit {
  product?: Product;
  isLoading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadProduct();
  }

  loadProduct(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    
    if (productId) {
      this.isLoading = true;
      this.productService.getProductById(Number(productId)).subscribe({
        next: (product) => {
          this.product = product;
          this.isLoading = false;
        },
        error: (error) => {
          this.isLoading = false;
          this.messageService.add({
            severity: 'error', 
            summary: 'Erro', 
            detail: 'Produto não encontrado'
          });
          this.router.navigate(['/produtos']);
        }
      });
    }
  }

  onProductUpdated(updatedProduct: Product): void {
    if (!this.product?.id) return;

    this.isLoading = true;
    this.productService.updateProduct(this.product.id, updatedProduct).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Produto atualizado com sucesso'
        });
        this.router.navigate(['/produtos']);
      },
      error: (error) => {
        this.isLoading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: error.message || 'Falha ao atualizar produto'
        });
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/produtos']);
  }
}