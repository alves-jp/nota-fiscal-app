import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InvoiceItemEditPageComponent } from './invoice-item-edit-page.component';
import { ActivatedRoute, Router } from '@angular/router';
import { InvoiceItemService } from '../../../../core/services/api/invoice-item.service';
import { MessageService } from 'primeng/api';
import { of, throwError } from 'rxjs';
import { InvoiceItem } from '../../../../core/models/invoice-item.model';
import { Product } from '../../../../core/models/product.model';

describe('InvoiceItemEditPageComponent', () => {
  let component: InvoiceItemEditPageComponent;
  let fixture: ComponentFixture<InvoiceItemEditPageComponent>;
  let invoiceItemService: jasmine.SpyObj<InvoiceItemService>;
  let router: jasmine.SpyObj<Router>;
  let route: ActivatedRoute;
  let messageService: jasmine.SpyObj<MessageService>;

  const mockItem: InvoiceItem = {
    id: 1,
    invoiceId: 1,
    product: { id: 1, productCode: 'P001', description: 'Produto 1', productStatus: 'ACTIVE' } as Product,
    unitPrice: 10.99,
    quantity: 2,
    totalPrice: 21.98
  };

  beforeEach(async () => {
    const invoiceItemServiceSpy = jasmine.createSpyObj('InvoiceItemService', ['getInvoiceItemById', 'updateInvoiceItem']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    const messageServiceSpy = jasmine.createSpyObj('MessageService', ['add']);

    await TestBed.configureTestingModule({
      imports: [InvoiceItemEditPageComponent],
      providers: [
        { provide: InvoiceItemService, useValue: invoiceItemServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: MessageService, useValue: messageServiceSpy },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => '1' } } }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(InvoiceItemEditPageComponent);
    component = fixture.componentInstance;
    invoiceItemService = TestBed.inject(InvoiceItemService) as jasmine.SpyObj<InvoiceItemService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    route = TestBed.inject(ActivatedRoute);
    messageService = TestBed.inject(MessageService) as jasmine.SpyObj<MessageService>;
    
    invoiceItemService.getInvoiceItemById.and.returnValue(of(mockItem));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load invoice item on init', () => {
    component.ngOnInit();
    expect(invoiceItemService.getInvoiceItemById).toHaveBeenCalledWith(1);
    expect(component.invoiceItem).toEqual(mockItem);
  });

  it('should handle error when loading item', () => {
    invoiceItemService.getInvoiceItemById.and.returnValue(throwError(() => new Error('Error')));
    component.ngOnInit();
    expect(messageService.add).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalled();
  });
});