import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InvoiceItemCreatePageComponent } from './invoice-item-create-page.component';
import { InvoiceItemService } from '../../../../core/services/api/invoice-item.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('InvoiceItemCreatePageComponent', () => {
  let component: InvoiceItemCreatePageComponent;
  let fixture: ComponentFixture<InvoiceItemCreatePageComponent>;
  let invoiceItemService: jasmine.SpyObj<InvoiceItemService>;
  let router: jasmine.SpyObj<Router>;
  let messageService: jasmine.SpyObj<MessageService>;

  beforeEach(async () => {
    const invoiceItemServiceSpy = jasmine.createSpyObj('InvoiceItemService', ['createInvoiceItem']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    const messageServiceSpy = jasmine.createSpyObj('MessageService', ['add']);

    await TestBed.configureTestingModule({
      imports: [InvoiceItemCreatePageComponent],
      providers: [
        { provide: InvoiceItemService, useValue: invoiceItemServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: MessageService, useValue: messageServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(InvoiceItemCreatePageComponent);
    component = fixture.componentInstance;
    component.invoiceId = 1;
    invoiceItemService = TestBed.inject(InvoiceItemService) as jasmine.SpyObj<InvoiceItemService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    messageService = TestBed.inject(MessageService) as jasmine.SpyObj<MessageService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  
  it('should handle cancel', () => {
    component.onCancel();
    expect(router.navigate).toHaveBeenCalledWith(['/notas-fiscais', 1]);
  });
});