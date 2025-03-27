import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InvoiceItemListComponent } from './invoice-item-list.component';
import { InvoiceItemService } from '../../../../core/services/api/invoice-item.service';
import { MessageService } from 'primeng/api';
import { of, throwError } from 'rxjs';
import { InvoiceItem } from '../../../../core/models/invoice-item.model';
import { Product } from '../../../../core/models/product.model';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';

describe('InvoiceItemListComponent', () => {
  let component: InvoiceItemListComponent;
  let fixture: ComponentFixture<InvoiceItemListComponent>;
  let invoiceItemService: jasmine.SpyObj<InvoiceItemService>;
  let messageService: jasmine.SpyObj<MessageService>;

  const mockItems: InvoiceItem[] = [
    {
      id: 1,
      invoiceId: 1,
      product: { id: 1, productCode: 'PROD-001', description: 'Produto 1', productStatus: 'ACTIVE' },
      unitValue: 10.99,
      quantity: 2,
      totalPrice: 21.98
    },
    {
      id: 2,
      invoiceId: 1,
      product: { id: 2, productCode: 'PROD-002', description: 'Produto 2', productStatus: 'ACTIVE' },
      unitValue: 15.50,
      quantity: 3,
      totalPrice: 46.50
    }
  ];

  beforeEach(async () => {
    const invoiceItemServiceSpy = jasmine.createSpyObj('InvoiceItemService', ['getItemsByInvoiceId', 'deleteInvoiceItem']);
    const messageServiceSpy = jasmine.createSpyObj('MessageService', ['add']);

    await TestBed.configureTestingModule({
      imports: [
        InvoiceItemListComponent,
        TableModule,
        ButtonModule,
        ToastModule
      ],
      providers: [
        { provide: InvoiceItemService, useValue: invoiceItemServiceSpy },
        { provide: MessageService, useValue: messageServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(InvoiceItemListComponent);
    component = fixture.componentInstance;
    component.invoiceId = 1;
    invoiceItemService = TestBed.inject(InvoiceItemService) as jasmine.SpyObj<InvoiceItemService>;
    messageService = TestBed.inject(MessageService) as jasmine.SpyObj<MessageService>;
    
    invoiceItemService.getItemsByInvoiceId.and.returnValue(of(mockItems));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load items on init', () => {
    expect(component.invoiceItems.length).toBe(2);
    expect(component.invoiceItems[0].product.productCode).toBe('PROD-001');
  });

  it('should handle error when loading items', () => {
    invoiceItemService.getItemsByInvoiceId.and.returnValue(throwError(() => new Error('Error')));
    component.loadInvoiceItems();
    expect(messageService.add).toHaveBeenCalled();
  });
});