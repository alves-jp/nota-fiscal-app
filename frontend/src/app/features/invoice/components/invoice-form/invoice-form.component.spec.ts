import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InvoiceFormComponent } from './invoice-form.component';
import { MessageService } from 'primeng/api';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';

describe('InvoiceFormComponent', () => {
  let component: InvoiceFormComponent;
  let fixture: ComponentFixture<InvoiceFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        FormsModule,
        ReactiveFormsModule,
        ToastModule,
        InputTextModule,
        CalendarModule,
        DropdownModule,
        ButtonModule
      ],
      providers: [MessageService]
    }).compileComponents();

    fixture = TestBed.createComponent(InvoiceFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with default values', () => {
    expect(component.invoiceForm.value).toEqual({
      id: null,
      invoiceNumber: '',
      issueDate: null,
      supplierId: null,
      address: ''
    });
  });

  it('should validate required fields', () => {
    const form = component.invoiceForm;
    expect(form.valid).toBeFalse();
    
    form.patchValue({
      invoiceNumber: 'NF123456',
      issueDate: new Date(),
      supplierId: 1,
      address: 'Rua Teste, 123'
    });
    expect(form.valid).toBeTrue();
  });
});