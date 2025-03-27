import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ValidatorFn, AbstractControl } from '@angular/forms';
import { Supplier, CompanyStatus } from '../../../../core/models/supplier.model';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';

@Component({
  selector: 'app-supplier-form',
  templateUrl: './supplier-form.component.html',
  styleUrls: ['./supplier-form.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    ToastModule,
    InputTextModule,
    ReactiveFormsModule,
    ButtonModule,
    CalendarModule
  ],
  providers: [MessageService]
})
export class SupplierFormComponent implements OnInit {
  @Input() supplier?: Supplier;
  @Output() formSubmit = new EventEmitter<Supplier>();
  @Output() formCancel = new EventEmitter<void>();

  supplierForm: FormGroup;
  statusOptions = Object.values(CompanyStatus);
  minDate = new Date();

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService
  ) {
    this.supplierForm = this.fb.group({
      id: [null],
      supplierCode: ['', [Validators.required, Validators.maxLength(20)]],
      companyName: ['', [Validators.required, Validators.maxLength(100)]],
      supplierEmail: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
      supplierPhone: ['', [Validators.required, Validators.maxLength(20)]],
      cnpj: ['', [Validators.required, Validators.maxLength(18)]],
      companyStatus: [CompanyStatus.ACTIVE, Validators.required],
      companyDeactivationDate: [null]
    });

    this.supplierForm.get('companyStatus')?.valueChanges.subscribe(status => {
      this.updateDeactivationDateValidator(status);
    });
  }

  ngOnInit(): void {
    if (this.supplier) {
      this.supplierForm.patchValue({
        id: this.supplier.id,
        supplierCode: this.supplier.supplierCode,
        companyName: this.supplier.companyName,
        supplierEmail: this.supplier.supplierEmail,
        supplierPhone: this.supplier.supplierPhone,
        cnpj: this.supplier.cnpj,
        companyStatus: this.supplier.companyStatus,
        companyDeactivationDate: this.supplier.companyDeactivationDate
      });
    }
  }

  private updateDeactivationDateValidator(status: CompanyStatus): void {
    const deactivationDateControl = this.supplierForm.get('companyDeactivationDate');
    
    if (status === CompanyStatus.INACTIVE) {
      deactivationDateControl?.setValidators([Validators.required]);
    } else {
      deactivationDateControl?.clearValidators();
      deactivationDateControl?.setValue(null);
    }
    
    deactivationDateControl?.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.supplierForm.valid) {
      this.formSubmit.emit(this.supplierForm.value);
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: 'Preencha todos os campos obrigatórios corretamente'
      });
      this.markAllAsTouched();
    }
  }

  onCancel(): void {
    this.formCancel.emit();
  }

  private markAllAsTouched(): void {
    Object.values(this.supplierForm.controls).forEach(control => {
      control.markAsTouched();
    });
  }
}