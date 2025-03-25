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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    
    if (productId) {
      this.productService.getProductById(Number(productId)).subscribe({
        next: (product) => {
          this.product = product;
        },
        error: (error) => {
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

  onProductUpdated(product: Product): void {
    this.router.navigate(['/produtos']);
  }

  onCancel(): void {
    this.router.navigate(['/produtos']);
  }
}
