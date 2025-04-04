import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Supplier, CompanyStatus } from '../../../../core/models/supplier.model';
import { MessageService } from 'primeng/api';
import { SupplierListComponent } from '../../components/supplier-list/supplier-list.component';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-supplier-list-page',
  templateUrl: './supplier-list-page.component.html',
  styleUrls: ['./supplier-list-page.component.scss'],
  standalone: true,
  imports: [
    SupplierListComponent, 
    ToastModule, 
    ButtonModule
  ]
})
export class SupplierListPageComponent {
  constructor(
    private router: Router,
    private messageService: MessageService
  ) {}

  onEditSupplier(supplier: Supplier): void {
    if (!supplier?.id) {
      console.error('Invalid supplier object:', supplier);
      this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: 'Não foi possível editar: ID do fornecedor não encontrado',
        life: 5000
      });
      return;
    }
    
    this.router.navigate(['/fornecedores/editar', supplier.id]);
  }

  onNewSupplier(): void {
    this.router.navigate(['/fornecedores/novo']);
  }

  onBackToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  handleSupplierCreated(event: { message: string }): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Sucesso',
      detail: event.message || 'Fornecedor criado com sucesso',
      life: 3000
    });
  }
}