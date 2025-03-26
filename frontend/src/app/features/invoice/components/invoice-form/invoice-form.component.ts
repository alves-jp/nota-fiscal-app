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
import { ButtonModule } from 'primeng/button';
import { AutoCompleteSelectEvent } from 'primeng/autocomplete';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { Supplier } from '../../../../core/models/supplier.model';

@Component({
  selector: 'app-invoice-form',
  templateUrl: './invoice-form.component.html',
  styleUrls: ['./invoice-form.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    ToastModule,
    InputTextModule,
    ReactiveFormsModule,
    CalendarModule,
    DropdownModule,
    ButtonModule,
    AutoCompleteModule
  ],
  providers: [MessageService]
})
export class InvoiceFormComponent implements OnInit {
  @Input() invoice?: InvoiceResponseDTO;
  @Input() suppliers: Supplier[] = [];
  @Output() formSubmit = new EventEmitter<InvoiceDTO>();
  @Output() formCancel = new EventEmitter<void>();

  invoiceForm: FormGroup;
  filteredSuppliers: Supplier[] = [];
  currentDate = new Date();
  loading = false;

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService
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
    if (this.invoice) {
      this.patchFormWithInvoiceData();
    }
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

  filterSuppliers(event: { query: string }): void {
    const query = event.query.toLowerCase();
    this.filteredSuppliers = this.suppliers.filter(supplier => 
      supplier.companyName.toLowerCase().includes(query) || 
      (supplier.cnpj && supplier.cnpj.toLowerCase().includes(query)) ||
      (supplier.supplierCode && supplier.supplierCode.toLowerCase().includes(query))
    );
  }

  onSupplierSelect(event: AutoCompleteSelectEvent): void {
    const supplier = event.value as Supplier;
    this.invoiceForm.patchValue({
      supplierId: supplier.id
    });
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
  }

  private formatDateForInput(date: Date | string): string {
    const d = new Date(date);
    return d.toISOString().split('T')[0];
  }

  onSubmit(): void {
    if (this.invoiceForm.invalid) {
      this.handleInvalidForm();
      return;
    }

    this.loading = true;
    const formValue = this.invoiceForm.value;
    const invoiceData: InvoiceDTO = {
      id: formValue.id,
      invoiceNumber: formValue.invoiceNumber,
      issueDate: this.prepareIssueDate(formValue.issueDate),
      supplierId: formValue.supplierId,
      address: formValue.address
    };
    this.formSubmit.emit(invoiceData);
  }

  private prepareIssueDate(dateString: string): Date {
    const date = new Date(dateString);
    const now = new Date();
    date.setHours(now.getHours(), now.getMinutes(), now.getSeconds());
    return date;
  }

  private handleInvalidForm(): void {
    this.messageService.add({
      severity: 'error',
      summary: 'Erro',
      detail: 'Preencha todos os campos obrigatórios'
    });
    this.markAllAsTouched();
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