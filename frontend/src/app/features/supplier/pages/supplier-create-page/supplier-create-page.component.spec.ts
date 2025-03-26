import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SupplierCreatePageComponent } from './supplier-create-page.component';
import { SupplierDTO, Supplier, CompanyStatus } from '../../../../core/models/supplier.model';
import { SupplierService } from '../../../../core/services/api/supplier.service';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { throwError } from 'rxjs';

describe('SupplierCreatePageComponent', () => {
  let component: SupplierCreatePageComponent;
  let fixture: ComponentFixture<SupplierCreatePageComponent>;
  let supplierService: jasmine.SpyObj<SupplierService>;
  let router: jasmine.SpyObj<Router>;
  let messageService: jasmine.SpyObj<MessageService>;

  beforeEach(async () => {
    const supplierServiceSpy = jasmine.createSpyObj('SupplierService', ['createSupplier']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    const messageServiceSpy = jasmine.createSpyObj('MessageService', ['add']);

    await TestBed.configureTestingModule({
      imports: [SupplierCreatePageComponent],
      providers: [
        { provide: SupplierService, useValue: supplierServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: MessageService, useValue: messageServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SupplierCreatePageComponent);
    component = fixture.componentInstance;
    supplierService = TestBed.inject(SupplierService) as jasmine.SpyObj<SupplierService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    messageService = TestBed.inject(MessageService) as jasmine.SpyObj<MessageService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});