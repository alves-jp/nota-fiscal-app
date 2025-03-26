import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InvoiceItemListPageComponent } from './invoice-item-list-page.component';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { InvoiceItemListComponent } from '../../components/invoice-item-list/invoice-item-list.component';
import { InvoiceItem } from '../../../../core/models/invoice-item.model';

class MockRouter {
  navigate = jasmine.createSpy('navigate');
}

class MockMessageService {
  add = jasmine.createSpy('add');
}

describe('InvoiceItemListPageComponent', () => {
  let component: InvoiceItemListPageComponent;
  let fixture: ComponentFixture<InvoiceItemListPageComponent>;
  let router: Router;
  let messageService: MessageService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InvoiceItemListPageComponent, InvoiceItemListComponent],
      providers: [
        { provide: Router, useClass: MockRouter },
        { provide: MessageService, useClass: MockMessageService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(InvoiceItemListPageComponent);
    component = fixture.componentInstance;
    component.invoiceId = 1; // Definindo o invoiceId para testes
    router = TestBed.inject(Router);
    messageService = TestBed.inject(MessageService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to edit item', () => {
    const mockItem = { id: 1 } as InvoiceItem;
    component.onEditItem(mockItem);
    expect(router.navigate).toHaveBeenCalledWith(['/notas-fiscais', 1, 'itens', 'editar', 1]);
  });

  it('should navigate to new item', () => {
    component.onNewItem();
    expect(router.navigate).toHaveBeenCalledWith(['/notas-fiscais', 1, 'itens', 'novo']);
  });

  it('should show success message', () => {
    component.handleItemCreated({ message: 'Teste' });
    expect(messageService.add).toHaveBeenCalled();
  });
});