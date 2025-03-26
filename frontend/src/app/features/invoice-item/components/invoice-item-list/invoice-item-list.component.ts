import { Component, OnInit, Output, EventEmitter, ViewChild, ElementRef, Input } from '@angular/core';
import { InvoiceItemService } from '../../../../core/services/api/invoice-item.service';
import { InvoiceItem } from '../../../../core/models/invoice-item.model';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-invoice-item-list',
  templateUrl: './invoice-item-list.component.html',
  styleUrls: ['./invoice-item-list.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    ToastModule
  ],
  providers: [MessageService]
})
export class InvoiceItemListComponent implements OnInit {
  @ViewChild('confirmDialog') confirmDialog!: ElementRef<HTMLDialogElement>;
  @Input() invoiceId!: number;
  invoiceItems: InvoiceItem[] = [];
  loading = true;
  dialogTitle = '';
  dialogMessage = '';
  
  @Output() editItem = new EventEmitter<InvoiceItem>();
  @Output() createSuccess = new EventEmitter<{ message: string }>();

  constructor(
    private invoiceItemService: InvoiceItemService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadInvoiceItems();
  }

  loadInvoiceItems(): void {
    this.loading = true;
    this.invoiceItemService.getItemsByInvoiceId(this.invoiceId).subscribe({
      next: (items) => {
        this.invoiceItems = items;
        this.loading = false;
      },
      error: () => {
        this.showError('Falha ao carregar itens');
        this.loading = false;
      }
    });
  }

  onEdit(item: InvoiceItem): void {
    this.editItem.emit(item);
  }

  async onDelete(item: InvoiceItem): Promise<void> {
    this.dialogTitle = 'Confirmar Exclusão';
    this.dialogMessage = `Deseja excluir o item do produto <strong>${item.product.productCode}</strong>?`;
    
    const confirmed = await this.openDialog();
    if (confirmed) this.deleteItem(item.id);
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

  private deleteItem(id: number): void {
    this.invoiceItemService.deleteInvoiceItem(id).subscribe({
      next: () => {
        this.loadInvoiceItems();
        this.showSuccess('Item excluído com sucesso');
      },
      error: (error: Error) => {
        this.showError(error.message || 'Falha ao excluir item');
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
}