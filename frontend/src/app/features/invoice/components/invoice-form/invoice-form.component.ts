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

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService
  ) {
    this.invoiceForm = this.fb.group({
      id: [null],
      invoiceNumber: ['', [Validators.required, Validators.maxLength(50)]],
      issueDate: [null, Validators.required],
      supplierId: [null, Validators.required],
      address: ['', [Validators.required, Validators.maxLength(255)]]
    });
  }

  ngOnInit(): void {
    if (this.invoice) {
      this.invoiceForm.patchValue({
        id: this.invoice.id,
        invoiceNumber: this.invoice.invoiceNumber,
        issueDate: new Date(this.invoice.issueDate),
        supplierId: this.invoice.supplier.id,
        address: this.invoice.address
      });
    }
  }

  onSubmit(): void {
    if (this.invoiceForm.valid) {
      const formValue = this.invoiceForm.value;
      const invoiceData: InvoiceDTO = {
        id: formValue.id,
        invoiceNumber: formValue.invoiceNumber,
        issueDate: formValue.issueDate,
        supplierId: formValue.supplierId,
        address: formValue.address
      };
      this.formSubmit.emit(invoiceData);
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: 'Preencha todos os campos obrigatórios'
      });
      this.markAllAsTouched();
    }
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