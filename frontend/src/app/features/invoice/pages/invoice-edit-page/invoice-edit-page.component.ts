import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { InvoiceService } from '../../../../core/services/api/invoice.service';
import { InvoiceResponseDTO, InvoiceDTO } from '../../../../core/models/invoice.model';
import { MessageService } from 'primeng/api';
import { InvoiceFormComponent } from '../../components/invoice-form/invoice-form.component';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-invoice-edit-page',
  templateUrl: './invoice-edit-page.component.html',
  styleUrls: ['./invoice-edit-page.component.scss'],
  standalone: true,
  imports: [CommonModule,
    ToastModule,
    InvoiceFormComponent],
  providers: [MessageService]
})
export class InvoiceEditPageComponent implements OnInit {
  invoice?: InvoiceResponseDTO;
  isLoading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private invoiceService: InvoiceService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadInvoice();
  }

  loadInvoice(): void {
    const invoiceId = this.route.snapshot.paramMap.get('id');
    
    if (invoiceId) {
      this.isLoading = true;
      this.invoiceService.getInvoiceById(Number(invoiceId)).subscribe({
        next: (invoice) => {
          this.invoice = invoice;
          this.isLoading = false;
        },
        error: (error) => {
          this.isLoading = false;
          this.messageService.add({
            severity: 'error', 
            summary: 'Erro', 
            detail: 'Nota fiscal não encontrada'
          });
          this.router.navigate(['/notas-fiscais']);
        }
      });
    }
  }

  onInvoiceUpdated(updatedInvoice: InvoiceDTO): void {
    if (!this.invoice?.id) return;
  
    if (this.invoice.supplier?.id !== updatedInvoice.supplierId) {
      this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: 'Não é permitido alterar o fornecedor de uma nota fiscal existente.',
        life: 5000
      });
      return;
    }
  
    this.isLoading = true;
    this.invoiceService.updateInvoice(this.invoice.id, updatedInvoice).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Nota fiscal atualizada com sucesso'
        });
        this.router.navigate(['/notas-fiscais']);
      },
      error: (error) => {
        this.isLoading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: error.message || 'Falha ao atualizar nota fiscal'
        });
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/notas-fiscais']);
  }
}