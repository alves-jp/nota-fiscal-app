import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { ProductService } from '../../../../core/services/api/product.service';
import { Product } from '../../../../core/models/product.model';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputNumberModule } from 'primeng/inputnumber';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ToastModule, TableModule, ConfirmDialogModule, InputNumberModule]
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  
  @Output() editProduct = new EventEmitter<Product>();

  constructor(
    private productService: ProductService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products;
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error', 
          summary: 'Erro', 
          detail: 'Falha ao carregar produtos'
        });
      }
    });
  }

  onEdit(product: Product): void {
    this.editProduct.emit(product);
  }

  onDelete(product: Product): void {
    this.confirmationService.confirm({
      message: `Tem certeza que deseja excluir o produto ${product.name}?`,
      header: 'Confirmação de Exclusão',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.productService.deleteProduct(product.id!).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success', 
              summary: 'Sucesso', 
              detail: 'Produto excluído'
            });
            this.loadProducts();
          },
          error: (error) => {
            this.messageService.add({
              severity: 'error', 
              summary: 'Erro', 
              detail: 'Falha ao excluir produto'
            });
          }
        });
      }
    });
  }
}