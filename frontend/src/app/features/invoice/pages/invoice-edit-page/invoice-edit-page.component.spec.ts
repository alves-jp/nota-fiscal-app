import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InvoiceEditPageComponent } from './invoice-edit-page.component';
import { ActivatedRoute, Router } from '@angular/router';
import { InvoiceService } from '../../../../core/services/api/invoice.service';
import { MessageService } from 'primeng/api';
import { of, throwError } from 'rxjs';
import { InvoiceResponseDTO } from '../../../../core/models/invoice.model';
import { CompanyStatus } from '../../../../core/models/supplier.model';

describe('InvoiceEditPageComponent', () => {
  let component: InvoiceEditPageComponent;
  let fixture: ComponentFixture<InvoiceEditPageComponent>;
  let invoiceService: jasmine.SpyObj<InvoiceService>;
  let router: jasmine.SpyObj<Router>;
  let messageService: jasmine.SpyObj<MessageService>;
  let route: ActivatedRoute;

  const mockInvoice: InvoiceResponseDTO = {
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
  };

  beforeEach(async () => {
    const invoiceServiceSpy = jasmine.createSpyObj('InvoiceService', ['getInvoiceById', 'updateInvoice']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    const messageServiceSpy = jasmine.createSpyObj('MessageService', ['add']);

    await TestBed.configureTestingModule({
      imports: [InvoiceEditPageComponent],
      providers: [
        { provide: InvoiceService, useValue: invoiceServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: MessageService, useValue: messageServiceSpy },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: () => '1'
              }
            }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(InvoiceEditPageComponent);
    component = fixture.componentInstance;
    invoiceService = TestBed.inject(InvoiceService) as jasmine.SpyObj<InvoiceService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    messageService = TestBed.inject(MessageService) as jasmine.SpyObj<MessageService>;
    route = TestBed.inject(ActivatedRoute);
  });
});