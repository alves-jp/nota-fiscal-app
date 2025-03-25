import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { ProductService } from '../../../../core/services/api/product.service';
import { Product } from '../../../../core/models/product.model';
import { ConfirmationService, MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { TagModule } from 'primeng/tag';

type PrimeSeverity = 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast';

interface StatusSeverityMap {
  ACTIVE: PrimeSeverity;
  INACTIVE: PrimeSeverity;
  PENDING: PrimeSeverity;
}

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    ConfirmDialogModule,
    ToastModule,
    TagModule
  ],
  providers: [ConfirmationService, MessageService]
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  loading = true;
  
  @Output() editProduct = new EventEmitter<Product>();
  @Output() createSuccess = new EventEmitter<{ message: string }>(); // Updated output

  readonly statusSeverity: StatusSeverityMap = {
    ACTIVE: 'success',
    INACTIVE: 'danger',
    PENDING: 'warn'
  };

  constructor(
    private productService: ProductService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  getSeverity(status: Product['productStatus']): PrimeSeverity {
    return this.statusSeverity[status];
  }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products;
        this.loading = false;
      },
      error: () => {
        this.showError('Falha ao carregar produtos');
        this.loading = false;
      }
    });
  }

  onEdit(product: Product): void {
    this.editProduct.emit(product);
  }

  onDelete(product: Product): void {
    this.confirmationService.confirm({
      message: `Deseja excluir o produto ${product.productCode}?`,
      header: 'Confirmar Exclusão',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: () => this.deleteProduct(product.id)
    });
  }

  private deleteProduct(id: number): void {
    this.productService.deleteProduct(id).subscribe({
      next: () => {
        this.showSuccess('Produto excluído');
        this.loadProducts();
        this.createSuccess.emit({ message: 'Produto excluído com sucesso' }); // Emitting success event
      },
      error: () => this.showError('Falha ao excluir produto')
    });
  }

  private showSuccess(message: string): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Sucesso',
      detail: message,
      life: 3000
    });
  }

  private showError(message: string): void {
    this.messageService.add({
      severity: 'error',
      summary: 'Erro',
      detail: message,
      life: 5000
    });
  }
}