import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductFormComponent } from '../../components/product-form/product-form.component';
import { Product } from '../../../../core/models/product.model';

@Component({
  selector: 'app-product-create-page',
  templateUrl: './product-create-page.component.html',
  styleUrls: ['./product-create-page.component.scss'],
  standalone: true,
  imports: [CommonModule, ProductFormComponent]
})
export class ProductCreatePageComponent {
  constructor(private router: Router) {}

  onProductCreated(product: Product): void {
    this.router.navigate(['/produtos']);
  }

  onCancel(): void {
    this.router.navigate(['/produtos']);
  }
}