import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SupplierService } from '../../../../core/services/api/supplier.service';
import {  Supplier, CompanyStatus } from '../../../../core/models/supplier.model';
import { MessageService } from 'primeng/api';
import { SupplierFormComponent } from '../../components/supplier-form/supplier-form.component';

@Component({
  selector: 'app-supplier-edit-page',
  templateUrl: './supplier-edit-page.component.html',
  styleUrls: ['./supplier-edit-page.component.scss'],
  standalone: true,
  imports: [CommonModule, SupplierFormComponent]
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
          this.messageService.add({
            severity: 'error', 
            summary: 'Erro', 
            detail: 'Fornecedor não encontrado'
          });
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
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Fornecedor atualizado com sucesso'
        });
        this.router.navigate(['/fornecedores']);
      },
      error: (error) => {
        this.isLoading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: error.message || 'Falha ao atualizar fornecedor'
        });
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/fornecedores']);
  }
}