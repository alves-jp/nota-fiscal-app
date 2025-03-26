import { Component, OnInit, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { SupplierService } from '../../../../core/services/api/supplier.service';
import { Supplier, CompanyStatus } from '../../../../core/models/supplier.model';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { TagModule } from 'primeng/tag';

type PrimeSeverity = 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast' | undefined;

@Component({
  selector: 'app-supplier-list',
  templateUrl: './supplier-list.component.html',
  styleUrls: ['./supplier-list.component.scss'],
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
export class SupplierListComponent implements OnInit {
  @ViewChild('confirmDialog') confirmDialog!: ElementRef<HTMLDialogElement>;
  suppliers: Supplier[] = [];
  loading = true;
  dialogTitle = '';
  dialogMessage = '';
  
  @Output() editSupplier = new EventEmitter<Supplier>();
  @Output() createSuccess = new EventEmitter<{ message: string }>();

  constructor(
    private supplierService: SupplierService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadSuppliers();
  }

  loadSuppliers(): void {
    this.loading = true;
    this.supplierService.getAllSuppliers().subscribe({
      next: (suppliers) => {
        this.suppliers = suppliers;
        this.loading = false;
      },
      error: () => {
        this.showError('Falha ao carregar fornecedores');
        this.loading = false;
      }
    });
  }

  onEdit(supplier: Supplier): void {
    console.log('Supplier object received:', supplier);
    
    if (!supplier?.id) {
      console.error('Cannot edit: supplier ID is missing', supplier);
      this.showError('Fornecedor inválido: ID não encontrado');
      return;
    }
    
    this.editSupplier.emit(supplier);
  }

  async onDelete(supplier: Supplier): Promise<void> {
    this.dialogTitle = 'Confirmar Exclusão';
    this.dialogMessage = `Deseja excluir o fornecedor <strong>${supplier.companyName}</strong>?`;
    
    const confirmed = await this.openDialog();
    if (confirmed) this.deleteSupplier(supplier.id);
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

  deleteSupplier(id: number): void {
    this.supplierService.deleteSupplier(id).subscribe({
      next: () => {
        this.showSuccess('Fornecedor excluído com sucesso');
        this.loadSuppliers();
      },
      error: (error: Error) => {
        this.showError(error.message || 'Falha ao excluir fornecedor');
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

  formatStatus(status: CompanyStatus): string {
    switch(status) {
      case CompanyStatus.ACTIVE: return 'Ativo';
      case CompanyStatus.SUSPENDED: return 'Suspenso';
      case CompanyStatus.INACTIVE: return 'Inativo';
      default: return status;
    }
  }

  getStatusSeverity(status: CompanyStatus): PrimeSeverity {
    switch(status) {
      case CompanyStatus.ACTIVE: return 'success';
      case CompanyStatus.SUSPENDED: return 'warn';
      case CompanyStatus.INACTIVE: return 'danger';
      default: return undefined;
    }
  }
}