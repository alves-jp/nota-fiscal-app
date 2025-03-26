import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SupplierEditPageComponent } from './supplier-edit-page.component';
import { ActivatedRoute, Router } from '@angular/router';
import { SupplierService } from '../../../../core/services/api/supplier.service';
import { MessageService } from 'primeng/api';
import { of, throwError } from 'rxjs';
import { Supplier, CompanyStatus } from '../../../../core/models/supplier.model';

describe('SupplierEditPageComponent', () => {
  let component: SupplierEditPageComponent;
  let fixture: ComponentFixture<SupplierEditPageComponent>;
  let supplierService: jasmine.SpyObj<SupplierService>;
  let router: jasmine.SpyObj<Router>;
  let messageService: jasmine.SpyObj<MessageService>;
  let route: ActivatedRoute;

  const mockSupplier: Supplier = {
    id: 1,
    supplierCode: 'FORN001',
    companyName: 'Fornecedor Teste',
    supplierEmail: 'teste@fornecedor.com',
    supplierPhone: '(11) 9999-9999',
    cnpj: '12.345.678/0001-90',
    companyStatus: CompanyStatus.ACTIVE
  };

  beforeEach(async () => {
    const supplierServiceSpy = jasmine.createSpyObj('SupplierService', ['getSupplierById', 'updateSupplier']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    const messageServiceSpy = jasmine.createSpyObj('MessageService', ['add']);

    await TestBed.configureTestingModule({
      imports: [SupplierEditPageComponent],
      providers: [
        { provide: SupplierService, useValue: supplierServiceSpy },
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

    fixture = TestBed.createComponent(SupplierEditPageComponent);
    component = fixture.componentInstance;
    supplierService = TestBed.inject(SupplierService) as jasmine.SpyObj<SupplierService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    messageService = TestBed.inject(MessageService) as jasmine.SpyObj<MessageService>;
    route = TestBed.inject(ActivatedRoute);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load supplier on init', () => {
    supplierService.getSupplierById.and.returnValue(of(mockSupplier));
    component.ngOnInit();
    expect(supplierService.getSupplierById).toHaveBeenCalledWith(1);
    expect(component.supplier).toEqual(mockSupplier);
  });

  it('should handle supplier not found', () => {
    supplierService.getSupplierById.and.returnValue(throwError(() => new Error('Not found')));
    component.ngOnInit();
    expect(messageService.add).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/fornecedores']);
  });


  it('should handle update error', () => {
    component.supplier = mockSupplier;
    supplierService.updateSupplier.and.returnValue(throwError(() => new Error('Error')));
    component.onSupplierUpdated(mockSupplier);
    expect(messageService.add).toHaveBeenCalled();
  });

  it('should handle cancel', () => {
    component.onCancel();
    expect(router.navigate).toHaveBeenCalledWith(['/fornecedores']);
  });
});