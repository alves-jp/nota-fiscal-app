import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SupplierListPageComponent } from './supplier-list-page.component';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { SupplierListComponent } from '../../components/supplier-list/supplier-list.component';
import { SupplierDTO, Supplier, CompanyStatus } from '../../../../core/models/supplier.model';

class MockRouter {
  navigate = jasmine.createSpy('navigate');
}

class MockMessageService {
  add = jasmine.createSpy('add');
}

describe('SupplierListPageComponent', () => {
  let component: SupplierListPageComponent;
  let fixture: ComponentFixture<SupplierListPageComponent>;
  let router: Router;
  let messageService: MessageService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierListPageComponent, SupplierListComponent],
      providers: [
        { provide: Router, useClass: MockRouter },
        { provide: MessageService, useClass: MockMessageService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SupplierListPageComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    messageService = TestBed.inject(MessageService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to edit on editSupplier', () => {
    const mockSupplier = { id: 1 } as Supplier;
    component.onEditSupplier(mockSupplier);
    expect(router.navigate).toHaveBeenCalledWith(['/fornecedores/editar', 1]);
  });

  it('should navigate to new on newSupplier', () => {
    component.onNewSupplier();
    expect(router.navigate).toHaveBeenCalledWith(['/fornecedores/novo']);
  });

  it('should show message on supplier created', () => {
    const event = { message: 'Success' };
    component.handleSupplierCreated(event);
    expect(messageService.add).toHaveBeenCalledWith(jasmine.objectContaining({
      severity: 'success',
      detail: 'Success'
    }));
  });
});