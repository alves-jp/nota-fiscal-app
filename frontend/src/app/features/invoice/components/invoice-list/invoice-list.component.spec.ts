import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InvoiceListComponent } from './invoice-list.component';
import { InvoiceService } from '../../../../core/services/api/invoice.service';
import { MessageService } from 'primeng/api';
import { of, throwError } from 'rxjs';
import { InvoiceResponseDTO } from '../../../../core/models/invoice.model';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { TagModule } from 'primeng/tag';
import { CompanyStatus } from '../../../../core/models/supplier.model';

describe('InvoiceListComponent', () => {
  let component: InvoiceListComponent;
  let fixture: ComponentFixture<InvoiceListComponent>;
  let invoiceService: jasmine.SpyObj<InvoiceService>;
  let messageService: jasmine.SpyObj<MessageService>;

  const mockInvoices: InvoiceResponseDTO[] = [
    {
      id: 1,
      invoiceNumber: 'NF-001',
      issueDate: new Date(),
      supplier: {
        id: 1, companyName: 'Fornecedor 1',
        supplierCode: '',
        supplierEmail: '',
        supplierPhone: '',
        cnpj: '',
        companyStatus: CompanyStatus.ACTIVE
      },
      address: 'Rua Teste, 123',
      items: [],
      totalValue: 100.50
    },
    {
      id: 2,
      invoiceNumber: 'NF-002',
      issueDate: new Date(),
      supplier: {
        id: 2, companyName: 'Fornecedor 2',
        supplierCode: '',
        supplierEmail: '',
        supplierPhone: '',
        cnpj: '',
        companyStatus: CompanyStatus.ACTIVE
      },
      address: 'Rua Teste, 456',
      items: [],
      totalValue: 200.75
    }
  ];

  beforeEach(async () => {
    const invoiceServiceSpy = jasmine.createSpyObj('InvoiceService', ['getAllInvoices', 'deleteInvoice']);
    const messageServiceSpy = jasmine.createSpyObj('MessageService', ['add']);

    await TestBed.configureTestingModule({
      imports: [
        InvoiceListComponent,
        TableModule,
        ButtonModule,
        ToastModule,
        TagModule
      ],
      providers: [
        { provide: InvoiceService, useValue: invoiceServiceSpy },
        { provide: MessageService, useValue: messageServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(InvoiceListComponent);
    component = fixture.componentInstance;
    invoiceService = TestBed.inject(InvoiceService) as jasmine.SpyObj<InvoiceService>;
    messageService = TestBed.inject(MessageService) as jasmine.SpyObj<MessageService>;
    
    invoiceService.getAllInvoices.and.returnValue(of(mockInvoices));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load invoices on init', () => {
    expect(invoiceService.getAllInvoices).toHaveBeenCalled();
    expect(component.invoices.length).toBe(2);
  });

  it('should handle error when loading invoices', () => {
    invoiceService.getAllInvoices.and.returnValue(throwError(() => new Error('Error')));
    component.loadInvoices();
    expect(messageService.add).toHaveBeenCalled();
  });

  it('should format date correctly', () => {
    const date = new Date(2023, 0, 1);
    expect(component.formatDate(date)).toContain('01/01/2023');
  });

  it('should format currency correctly', () => {
    expect(component.formatCurrency(100.5)).toContain('R$');
    expect(component.formatCurrency(100.5)).toContain('100,50');
  });
});