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
    ButtonModule
  ],
  providers: [MessageService]
})
export class InvoiceFormComponent implements OnInit {
  @Input() invoice?: InvoiceResponseDTO;
  @Input() suppliers: Supplier[] = [];
  @Output() formSubmit = new EventEmitter<InvoiceDTO>();
  @Output() formCancel = new EventEmitter<void>();

  invoiceForm: FormGroup;
  minDate = new Date();
  maxDate = new Date(new Date().setFullYear(new Date().getFullYear() + 1));

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService
  ) {
    this.invoiceForm = this.fb.group({
      id: [null],
      invoiceNumber: ['', [Validators.required, Validators.maxLength(50)]],
      issueDate: [null, [Validators.required]],
      supplierId: [null, Validators.required],
      address: ['', [Validators.required, Validators.maxLength(255)]]
    });
  }

  ngOnInit(): void {
    if (this.invoice) {
      this.patchFormWithInvoiceData();
    }
  }

  private patchFormWithInvoiceData(): void {
    if (!this.invoice) return;

    this.invoiceForm.patchValue({
      id: this.invoice.id,
      invoiceNumber: this.invoice.invoiceNumber,
      issueDate: this.invoice.issueDate ? new Date(this.invoice.issueDate) : null,
      supplierId: this.invoice.supplier?.id || null,
      address: this.invoice.address
    });
  }

  onSubmit(): void {
    if (this.invoiceForm.invalid) {
      this.handleInvalidForm();
      return;
    }

    const invoiceData = this.prepareInvoiceData();
    this.formSubmit.emit(invoiceData);
  }

  private handleInvalidForm(): void {
    this.messageService.add({
      severity: 'error',
      summary: 'Erro',
      detail: 'Preencha todos os campos obrigatórios'
    });
    this.markAllAsTouched();
  }

  private prepareInvoiceData(): InvoiceDTO {
    const formValue = this.invoiceForm.value;
    return {
      id: formValue.id,
      invoiceNumber: formValue.invoiceNumber,
      issueDate: formValue.issueDate,
      supplierId: formValue.supplierId,
      address: formValue.address
    };
  }

  onCancel(): void {
    this.formCancel.emit();
  }

  private markAllAsTouched(): void {
    Object.values(this.invoiceForm.controls).forEach(control => {
      control.markAsTouched();
    });
  }
}