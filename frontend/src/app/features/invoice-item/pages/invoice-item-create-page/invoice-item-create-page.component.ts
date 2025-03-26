import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InvoiceItemFormComponent } from '../../components/invoice-item-form/invoice-item-form.component';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { InvoiceItemDTO } from '../../../../core/models/invoice-item.model';
import { InvoiceItemService } from '../../../../core/services/api/invoice-item.service';

@Component({
  selector: 'app-invoice-item-create-page',
  templateUrl: './invoice-item-create-page.component.html',
  styleUrls: ['./invoice-item-create-page.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    InvoiceItemFormComponent,
    ToastModule
  ],
  providers: [MessageService]
})
export class InvoiceItemCreatePageComponent {
  @Input() invoiceId!: number;
  loading = false;
  submitted = false;

  constructor(
    private invoiceItemService: InvoiceItemService,
    private router: Router,
    private messageService: MessageService
  ) {}

  handleFormSubmit(itemData: InvoiceItemDTO): void {
    this.submitted = true;
    this.loading = true;

    this.invoiceItemService.createInvoiceItem(itemData).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Item adicionado com sucesso',
          life: 3000
        });
        setTimeout(() => this.router.navigate(['/notas-fiscais', this.invoiceId]), 1500);
      },
      error: (error) => {
        this.loading = false;
        const errorMessage = error.error?.message || 'Erro ao adicionar item';
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
    this.router.navigate(['/notas-fiscais', this.invoiceId]);
  }
}