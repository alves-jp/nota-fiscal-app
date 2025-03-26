import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { InvoiceResponseDTO } from '../../../../core/models/invoice.model';
import { MessageService } from 'primeng/api';
import { InvoiceListComponent } from '../../components/invoice-list/invoice-list.component';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-invoice-list-page',
  templateUrl: './invoice-list-page.component.html',
  styleUrls: ['./invoice-list-page.component.scss'],
  standalone: true,
  imports: [
    InvoiceListComponent, 
    ToastModule, 
    ButtonModule
  ]
})
export class InvoiceListPageComponent {
  constructor(
    private router: Router,
    private messageService: MessageService
  ) {}

  onEditInvoice(invoice: InvoiceResponseDTO): void {
    this.router.navigate(['/notas-fiscais/editar', invoice.id]);
  }

  onNewInvoice(): void {
    this.router.navigate(['/notas-fiscais/novo']);
  }

  handleInvoiceCreated(event: { message: string }): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Sucesso',
      detail: event.message || 'Nota fiscal criada com sucesso',
      life: 3000
    });
  }
}