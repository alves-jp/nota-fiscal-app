import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InvoiceCreatePageComponent } from './invoice-create-page.component';
import { InvoiceService } from '../../../../core/services/api/invoice.service';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { throwError } from 'rxjs';

describe('InvoiceCreatePageComponent', () => {
  let component: InvoiceCreatePageComponent;
  let fixture: ComponentFixture<InvoiceCreatePageComponent>;
  let invoiceService: jasmine.SpyObj<InvoiceService>;
  let router: jasmine.SpyObj<Router>;
  let messageService: jasmine.SpyObj<MessageService>;

  beforeEach(async () => {
    const invoiceServiceSpy = jasmine.createSpyObj('InvoiceService', ['createInvoice']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    const messageServiceSpy = jasmine.createSpyObj('MessageService', ['add']);

    await TestBed.configureTestingModule({
      imports: [InvoiceCreatePageComponent],
      providers: [
        { provide: InvoiceService, useValue: invoiceServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: MessageService, useValue: messageServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(InvoiceCreatePageComponent);
    component = fixture.componentInstance;
    invoiceService = TestBed.inject(InvoiceService) as jasmine.SpyObj<InvoiceService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    messageService = TestBed.inject(MessageService) as jasmine.SpyObj<MessageService>;
    fixture.detectChanges();
  });
});