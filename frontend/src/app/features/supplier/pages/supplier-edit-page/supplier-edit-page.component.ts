import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SupplierService } from '../../../../core/services/api/supplier.service';
import { Supplier } from '../../../../core/models/supplier.model';
import { MessageService } from 'primeng/api';
import { SupplierFormComponent } from '../../components/supplier-form/supplier-form.component';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-supplier-edit-page',
  templateUrl: './supplier-edit-page.component.html',
  styleUrls: ['./supplier-edit-page.component.scss'],
  standalone: true,
  imports: [CommonModule, SupplierFormComponent, ToastModule],
  providers: [MessageService]
})
export class SupplierEditPageComponent implements OnInit {
  supplier?: Supplier;
  isLoading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private supplierService: SupplierService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadSupplier();
  }

  loadSupplier(): void {
    const supplierId = this.route.snapshot.paramMap.get('id');
    
    if (supplierId) {
      this.isLoading = true;
      this.supplierService.getSupplierById(Number(supplierId)).subscribe({
        next: (supplier) => {
          this.supplier = supplier;
          this.isLoading = false;
        },
        error: (error) => {
          this.isLoading = false;
          this.showError('Fornecedor não encontrado');
          this.router.navigate(['/fornecedores']);
        }
      });
    }
  }

  onSupplierUpdated(updatedSupplier: Supplier): void {
    if (!this.supplier?.id) return;

    this.isLoading = true;
    this.supplierService.updateSupplier(this.supplier.id, updatedSupplier).subscribe({
      next: () => {
        this.showSuccess('Fornecedor atualizado com sucesso');
        setTimeout(() => this.router.navigate(['/fornecedores']), 1500);
      },
      error: (error) => {
        this.isLoading = false;
        const message = this.getErrorMessage(error);
        this.showError(message);
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/fornecedores']);
  }

  private getErrorMessage(error: any): string {
    return error.error?.message || 
           error.error ||
           error.message || 
           'Falha ao atualizar fornecedor';
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