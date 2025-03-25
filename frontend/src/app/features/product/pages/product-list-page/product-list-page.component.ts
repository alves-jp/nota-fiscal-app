import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../../../../core/models/product.model';
import { ProductListComponent } from '../../components/product-list/product-list.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-product-list-page',
  templateUrl: './product-list-page.component.html',
  styleUrls: ['./product-list-page.component.scss'],
  standalone: true,
  imports: [CommonModule, ProductListComponent]
})
export class ProductListPageComponent {
  constructor(private router: Router) {}

  onEditProduct(product: Product): void {
    this.router.navigate(['/produtos/editar', product.id]);
  }

  onNewProduct(): void {
    this.router.navigate(['/produtos/novo']);
  }
}