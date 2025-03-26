import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SupplierListComponent } from './supplier-list.component';
import { SupplierService } from '../../../../core/services/api/supplier.service';
import { MessageService } from 'primeng/api';
import { of, throwError } from 'rxjs';
import { Supplier, CompanyStatus } from '../../../../core/models/supplier.model';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { TagModule } from 'primeng/tag';

describe('SupplierListComponent', () => {
  let component: SupplierListComponent;
  let fixture: ComponentFixture<SupplierListComponent>;
  let supplierService: jasmine.SpyObj<SupplierService>;
  let messageService: jasmine.SpyObj<MessageService>;

  const mockSuppliers: Supplier[] = [
    {
      id: 1,
      supplierCode: 'FORN001',
      companyName: 'Fornecedor 1',
      supplierEmail: 'fornecedor1@teste.com',
      supplierPhone: '(11) 9999-9999',
      cnpj: '12.345.678/0001-90',
      companyStatus: CompanyStatus.ACTIVE
    },
    {
      id: 2,
      supplierCode: 'FORN002',
      companyName: 'Fornecedor 2',
      supplierEmail: 'fornecedor2@teste.com',
      supplierPhone: '(11) 8888-8888',
      cnpj: '98.765.432/0001-10',
      companyStatus: CompanyStatus.INACTIVE
    }
  ];

  beforeEach(async () => {
    const supplierServiceSpy = jasmine.createSpyObj('SupplierService', ['getAllSuppliers', 'deleteSupplier']);
    const messageServiceSpy = jasmine.createSpyObj('MessageService', ['add']);

    await TestBed.configureTestingModule({
      imports: [
        SupplierListComponent,
        TableModule,
        ButtonModule,
        ToastModule,
        TagModule
      ],
      providers: [
        { provide: SupplierService, useValue: supplierServiceSpy },
        { provide: MessageService, useValue: messageServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SupplierListComponent);
    component = fixture.componentInstance;
    supplierService = TestBed.inject(SupplierService) as jasmine.SpyObj<SupplierService>;
    messageService = TestBed.inject(MessageService) as jasmine.SpyObj<MessageService>;
    
    supplierService.getAllSuppliers.and.returnValue(of(mockSuppliers));
    fixture.detectChanges();
  });
});