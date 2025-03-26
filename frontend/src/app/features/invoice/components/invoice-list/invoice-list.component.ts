import { Component, OnInit, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { InvoiceService } from '../../../../core/services/api/invoice.service';
import { InvoiceResponseDTO } from '../../../../core/models/invoice.model';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { TagModule } from 'primeng/tag';

type PrimeSeverity = 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast';

@Component({
  selector: 'app-invoice-list',
  templateUrl: './invoice-list.component.html',
  styleUrls: ['./invoice-list.component.scss'],
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
export class InvoiceListComponent implements OnInit {
  @ViewChild('confirmDialog') confirmDialog!: ElementRef<HTMLDialogElement>;
  invoices: InvoiceResponseDTO[] = [];
  loading = true;
  dialogTitle = '';
  dialogMessage = '';
  
  @Output() editInvoice = new EventEmitter<InvoiceResponseDTO>();
  @Output() createSuccess = new EventEmitter<{ message: string }>();

  constructor(
    private invoiceService: InvoiceService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadInvoices();
  }

  loadInvoices(): void {
    this.loading = true;
    this.invoiceService.getAllInvoices().subscribe({
      next: (invoices) => {
        this.invoices = invoices;
        this.loading = false;
      },
      error: () => {
        this.showError('Falha ao carregar notas fiscais');
        this.loading = false;
      }
    });
  }

  onEdit(invoice: InvoiceResponseDTO): void {
    this.editInvoice.emit(invoice);
  }

  async onDelete(invoice: InvoiceResponseDTO): Promise<void> {
    this.dialogTitle = 'Confirmar Exclusão';
    this.dialogMessage = `Deseja excluir a nota fiscal <strong>${invoice.invoiceNumber}</strong>?`;
    
    const confirmed = await this.openDialog();
    if (confirmed) this.deleteInvoice(invoice.id);
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

  private deleteInvoice(id: number): void {
    this.invoiceService.deleteInvoice(id).subscribe({
      next: () => {
        this.loadInvoices();
        this.showSuccess('Nota fiscal excluída com sucesso');
      },
      error: (error: Error) => {
        this.showError(error.message || 'Falha ao excluir nota fiscal');
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

  formatDate(date: Date): string {
    return new Date(date).toLocaleDateString('pt-BR');
  }

  formatCurrency(value: number): string {
    return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
}