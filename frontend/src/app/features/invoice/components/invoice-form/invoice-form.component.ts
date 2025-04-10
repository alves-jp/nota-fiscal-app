import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InvoiceDTO, InvoiceResponseDTO } from '../../../../core/models/invoice.model';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { ReactiveFormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { Supplier } from '../../../../core/models/supplier.model';
import { SupplierService } from '../../../../core/services/api/supplier.service';

@Component({
  selector: 'app-invoice-form',
  templateUrl: './invoice-form.component.html',
  styleUrls: ['./invoice-form.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    ToastModule,
    FormsModule,
    InputTextModule,
    ReactiveFormsModule,
    CalendarModule,
    DropdownModule,
    ButtonModule
  ],
  providers: [MessageService]
})
export class InvoiceFormComponent implements OnInit {
  @Input() invoice?: InvoiceResponseDTO;
  @Input() loading: boolean = false;
  @Output() formSubmit = new EventEmitter<InvoiceDTO>();
  @Output() formCancel = new EventEmitter<void>();

  invoiceForm: FormGroup;
  filteredSuppliers: Supplier[] = [];
  currentDate = new Date();
  suppliers: Supplier[] = [];
  searchQuery: string = '';
  showSuggestions: boolean = false;
  selectedSupplier: Supplier | null = null;

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private supplierService: SupplierService
  ) {
    this.invoiceForm = this.fb.group({
      id: [null],
      invoiceNumber: ['', [Validators.required, Validators.maxLength(50)]],
      issueDate: [null, [Validators.required, this.pastDateValidator.bind(this)]],
      supplierId: [null, Validators.required],
      address: ['', [Validators.required, Validators.maxLength(255)]]
    });
  }

  ngOnInit(): void {
    this.loadSuppliers();
    
    if (this.invoice) {
      this.patchFormWithInvoiceData();
    }
  }

  private loadSuppliers(): void {
    this.supplierService.getAllSuppliers().subscribe({
      next: (suppliers) => {
        this.suppliers = suppliers;
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: 'Não foi possível carregar os fornecedores'
        });
      }
    });
  }

  onSupplierSearchInput(event: Event): void {
    const query = (event.target as HTMLInputElement).value;
    this.searchQuery = query;
    this.showSuggestions = query.length > 0;
    
    if (!this.showSuggestions) {
      this.filteredSuppliers = [];
      return;
    }
    
    const searchTerm = query.toLowerCase();
    this.filteredSuppliers = this.suppliers.filter(supplier => 
      supplier.companyName.toLowerCase().includes(searchTerm) || 
      (supplier.cnpj && supplier.cnpj.includes(searchTerm)) ||
      (supplier.supplierCode && supplier.supplierCode.toLowerCase().includes(searchTerm))
    );
  }

  selectSupplier(supplier: Supplier): void {
    this.selectedSupplier = supplier;
    this.invoiceForm.patchValue({
      supplierId: supplier.id
    });
    
    this.searchQuery = supplier.companyName;
    this.showSuggestions = false;
  }

  private pastDateValidator(control: any): { [key: string]: boolean } | null {
    if (!control.value) return null;
    
    const selectedDate = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    if (selectedDate > today) {
      return { 'futureDate': true };
    }
    return null;
  }

  private patchFormWithInvoiceData(): void {
    if (!this.invoice) return;

    this.invoiceForm.patchValue({
      id: this.invoice.id,
      invoiceNumber: this.invoice.invoiceNumber,
      issueDate: this.formatDateForInput(this.invoice.issueDate),
      supplierId: this.invoice.supplier?.id || null,
      address: this.invoice.address
    });

    if (this.invoice.supplier) {
      this.selectedSupplier = this.invoice.supplier;
      this.searchQuery = this.invoice.supplier.companyName;
    }
  }

  private formatDateForInput(date: Date | string): string {
    const d = new Date(date);
    return d.toISOString().split('T')[0];
  }

  private prepareIssueDate(dateString: string): Date {
    const date = new Date(dateString);
    const utcDate = new Date(Date.UTC(
      date.getFullYear(), 
      date.getMonth(), 
      date.getDate()
    ));
    return utcDate;
  }

  onSubmit(): void {
    if (this.invoiceForm.invalid) {
      this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: 'Por favor, preencha todos os campos obrigatórios corretamente'
      });
  
      this.markAllAsTouched();
      return;
    }
  
    const formValue = this.invoiceForm.getRawValue();
    const invoiceData: InvoiceDTO = {
      id: formValue.id,
      invoiceNumber: formValue.invoiceNumber,
      issueDate: this.prepareIssueDate(formValue.issueDate),
      supplierId: formValue.supplierId,
      address: formValue.address
    };
  
    this.formSubmit.emit(invoiceData);
  }

  private markAllAsTouched(): void {
    Object.values(this.invoiceForm.controls).forEach(control => {
      control.markAsTouched();
    });
  }

  onCancel(): void {
    this.formCancel.emit();
  }
}