import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InvoiceListPageComponent } from './invoice-list-page.component';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { InvoiceListComponent } from '../../components/invoice-list/invoice-list.component';
import { InvoiceResponseDTO } from '../../../../core/models/invoice.model';

class MockRouter {
  navigate = jasmine.createSpy('navigate');
}

class MockMessageService {
  add = jasmine.createSpy('add');
}

describe('InvoiceListPageComponent', () => {
  let component: InvoiceListPageComponent;
  let fixture: ComponentFixture<InvoiceListPageComponent>;
  let router: Router;
  let messageService: MessageService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InvoiceListPageComponent, InvoiceListComponent],
      providers: [
        { provide: Router, useClass: MockRouter },
        { provide: MessageService, useClass: MockMessageService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(InvoiceListPageComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    messageService = TestBed.inject(MessageService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to edit on editInvoice', () => {
    const mockInvoice = { id: 1 } as InvoiceResponseDTO;
    component.onEditInvoice(mockInvoice);
    expect(router.navigate).toHaveBeenCalledWith(['/notas-fiscais/editar', 1]);
  });

  it('should navigate to new on newInvoice', () => {
    component.onNewInvoice();
    expect(router.navigate).toHaveBeenCalledWith(['/notas-fiscais/novo']);
  });

  it('should show message on invoice created', () => {
    const event = { message: 'Success' };
    component.handleInvoiceCreated(event);
    expect(messageService.add).toHaveBeenCalledWith(jasmine.objectContaining({
      severity: 'success',
      detail: 'Success'
    }));
  });
});