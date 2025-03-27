import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
export class InvoiceItemListPageComponent implements OnInit {
  invoiceId!: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.invoiceId = Number(this.route.snapshot.paramMap.get('invoiceId'));
  }

  onEditItem(item: InvoiceItem): void {
    this.router.navigate(['/notas-fiscais', this.invoiceId, 'itens', 'editar', item.id]);
  }

  onNewItem(): void {
    this.router.navigate(['/notas-fiscais', this.invoiceId, 'itens', 'novo']);
  }

  onBackToDashboard(): void {
    this.router.navigate(['/notas-fiscais']);
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