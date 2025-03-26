import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { InvoiceItem } from '../../../../core/models/invoice-item.model';
import { MessageService } from 'primeng/api';
import { InvoiceItemListComponent } from '../../components/invoice-item-list/invoice-item-list.component';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-invoice-item-list-page',
  templateUrl: './invoice-item-list-page.component.html',
  styleUrls: ['./invoice-item-list-page.component.scss'],
  standalone: true,
  imports: [
    InvoiceItemListComponent, 
    ToastModule, 
    ButtonModule
  ],
  providers: [MessageService]
})
export class InvoiceItemListPageComponent {
  @Input() invoiceId!: number;

  constructor(
    private router: Router,
    private messageService: MessageService
  ) {}

  onEditItem(item: InvoiceItem): void {
    this.router.navigate(['/notas-fiscais', this.invoiceId, 'itens', 'editar', item.id]);
  }

  onNewItem(): void {
    this.router.navigate(['/notas-fiscais', this.invoiceId, 'itens', 'novo']);
  }

  handleItemCreated(event: { message: string }): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Sucesso',
      detail: event.message || 'Item criado com sucesso',
      life: 3000
    });
  }
}