import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../../../../core/models/product.model';
import { MessageService } from 'primeng/api';
import { ProductListComponent } from '../../components/product-list/product-list.component';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-product-list-page',
  templateUrl: './product-list-page.component.html',
  styleUrls: ['./product-list-page.component.scss'],
  standalone: true,
  imports: [
    ProductListComponent, 
    ToastModule, 
    ButtonModule
  ]
})
export class ProductListPageComponent {
  constructor(
    private router: Router,
    private messageService: MessageService
  ) {}

  onEditProduct(product: Product): void {
    this.router.navigate(['/produtos/editar', product.id]);
  }

  onNewProduct(): void {
    this.router.navigate(['/produtos/novo']);
  }

  onBackToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  handleProductCreated(event: { message: string }): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Sucesso',
      detail: event.message || 'Produto criado com sucesso',
      life: 3000
    });
  }
}