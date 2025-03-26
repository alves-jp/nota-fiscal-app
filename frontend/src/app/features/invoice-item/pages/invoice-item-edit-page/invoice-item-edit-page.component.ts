import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { InvoiceItemService } from '../../../../core/services/api/invoice-item.service';
import { InvoiceItem, InvoiceItemDTO } from '../../../../core/models/invoice-item.model';
import { MessageService } from 'primeng/api';
import { InvoiceItemFormComponent } from '../../components/invoice-item-form/invoice-item-form.component';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-invoice-item-edit-page',
  templateUrl: './invoice-item-edit-page.component.html',
  styleUrls: ['./invoice-item-edit-page.component.scss'],
  standalone: true,
  imports: [
    CommonModule, 
    InvoiceItemFormComponent,
    ToastModule
  ],
  providers: [MessageService]
})
export class InvoiceItemEditPageComponent implements OnInit {
  invoiceItem?: InvoiceItem;
  isLoading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private invoiceItemService: InvoiceItemService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadInvoiceItem();
  }

  loadInvoiceItem(): void {
    const itemId = this.route.snapshot.paramMap.get('id');
    
    if (itemId) {
      this.isLoading = true;
      this.invoiceItemService.getInvoiceItemById(Number(itemId)).subscribe({
        next: (item) => {
          this.invoiceItem = item;
          this.isLoading = false;
        },
        error: (error) => {
          this.isLoading = false;
          this.messageService.add({
            severity: 'error', 
            summary: 'Erro', 
            detail: 'Item não encontrado',
            life: 5000
          });
          this.navigateBackToList();
        }
      });
    }
  }

  onItemUpdated(itemDTO: InvoiceItemDTO): void {
    if (!this.invoiceItem?.id) return;

    this.isLoading = true;
    
    this.invoiceItemService.updateInvoiceItem(this.invoiceItem.id, itemDTO).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Item atualizado com sucesso',
          life: 3000
        });
        this.navigateBackToList();
      },
      error: (error) => {
        this.isLoading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: error.message || 'Falha ao atualizar item',
          life: 5000
        });
      }
    });
  }

  onCancel(): void {
    this.navigateBackToList();
  }

  private navigateBackToList(): void {
    if (this.invoiceItem?.invoiceId) {
      this.router.navigate(['/notas-fiscais', this.invoiceItem.invoiceId]);
    } else {
      this.router.navigate(['/notas-fiscais']);
    }
  }
}