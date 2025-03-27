import { Component, OnInit, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { ProductService } from '../../../../core/services/api/product.service';
import { Product } from '../../../../core/models/product.model';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
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
    ToastModule,
    TagModule
  ],
  providers: [MessageService]
})
export class ProductListComponent implements OnInit {
  @ViewChild('confirmDialog') confirmDialog!: ElementRef<HTMLDialogElement>;
  products: Product[] = [];
  loading = true;
  dialogTitle = '';
  dialogMessage = '';
  
  @Output() editProduct = new EventEmitter<Product>();
  @Output() createSuccess = new EventEmitter<{ message: string }>();

  readonly statusSeverity: StatusSeverityMap = {
    ACTIVE: 'success',
    INACTIVE: 'danger',
    PENDING: 'warn'
  };

  constructor(
    private productService: ProductService,
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

  async onDelete(product: Product): Promise<void> {
    this.dialogTitle = 'Confirmar Exclusão';
    this.dialogMessage = `Deseja excluir o produto <strong>${product.productCode}</strong>?`;
    
    const confirmed = await this.openDialog();
    if (confirmed) this.deleteProduct(product.id);
  }

  private openDialog(): Promise<boolean> {
    return new Promise((resolve) => {
      const dialog = this.confirmDialog.nativeElement;
      dialog.showModal();
      
      const handleClose = () => {
        resolve(dialog.returnValue === 'true');
        dialog.removeEventListener('close', handleClose);
      };
      
      dialog.addEventListener('close', handleClose);
    });
  }

  dialogConfirm(result: boolean): void {
    this.confirmDialog.nativeElement.close(result.toString());
  }

  private deleteProduct(id: number): void {
    this.productService.deleteProduct(id).subscribe({
      next: () => {
        this.loadProducts();
        this.showSuccess('Produto excluído com sucesso');
      },
      error: (error: Error) => {
        this.showError(error.message || 'Falha ao excluir produto');
      }
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

  formatStatus(status: Product['productStatus']): string {
    switch(status) {
      case 'ACTIVE': return 'Ativo';
      case 'INACTIVE': return 'Inativo';
      default: return status;
    }
  }
}