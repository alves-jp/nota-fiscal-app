import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SupplierFormComponent } from '../../components/supplier-form/supplier-form.component';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { Supplier, CompanyStatus } from '../../../../core/models/supplier.model';
import { SupplierService } from '../../../../core/services/api/supplier.service';

@Component({
  selector: 'app-supplier-create-page',
  templateUrl: './supplier-create-page.component.html',
  styleUrls: ['./supplier-create-page.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    SupplierFormComponent,
    CardModule,
    ToastModule
  ],
  providers: [MessageService]
})
export class SupplierCreatePageComponent {
  loading = false;
  submitted = false;

  constructor(
    private supplierService: SupplierService,
    private router: Router,
    private messageService: MessageService
  ) {}

  handleFormSubmit(supplier: Supplier): void {
    this.submitted = true;
    this.loading = true;

    this.supplierService.createSupplier(supplier).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Fornecedor criado com sucesso',
          life: 3000
        });
        setTimeout(() => this.router.navigate(['/fornecedores']), 1500);
      },
      error: (error) => {
        this.loading = false;
        const errorMessage = error.error?.message || 'Erro ao criar fornecedor';
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
    this.router.navigate(['/fornecedores']);
  }
}