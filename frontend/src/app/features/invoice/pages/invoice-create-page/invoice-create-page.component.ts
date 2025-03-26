import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InvoiceFormComponent } from '../../components/invoice-form/invoice-form.component';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { InvoiceDTO } from '../../../../core/models/invoice.model';
import { InvoiceService } from '../../../../core/services/api/invoice.service';

@Component({
  selector: 'app-invoice-create-page',
  templateUrl: './invoice-create-page.component.html',
  styleUrls: ['./invoice-create-page.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    InvoiceFormComponent,
    CardModule,
    ToastModule
  ],
  providers: [MessageService]
})
export class InvoiceCreatePageComponent {
  loading = false;
  submitted = false;

  constructor(
    private invoiceService: InvoiceService,
    private router: Router,
    private messageService: MessageService
  ) {}

  handleFormSubmit(invoice: InvoiceDTO): void {
    this.submitted = true;
    this.loading = true;

    this.invoiceService.createInvoice(invoice).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Nota fiscal criada com sucesso',
          life: 3000
        });
        setTimeout(() => this.router.navigate(['/notas-fiscais']), 1500);
      },
      error: (error) => {
        this.loading = false;
        const errorMessage = error.error?.message || 'Erro ao criar nota fiscal';
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: errorMessage,
          life: 5000
        });
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/notas-fiscais']);
  }
}