import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InvoiceItemFormComponent } from './invoice-item-form.component';
import { MessageService } from 'primeng/api';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { Product } from '../../../../core/models/product.model';

describe('InvoiceItemFormComponent', () => {
  let component: InvoiceItemFormComponent;
  let fixture: ComponentFixture<InvoiceItemFormComponent>;

  const mockProducts: Product[] = [
    { id: 1, productCode: 'P001', description: 'Produto 1', productStatus: 'ACTIVE' },
    { id: 2, productCode: 'P002', description: 'Produto 2', productStatus: 'ACTIVE' }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        FormsModule,
        ReactiveFormsModule,
        DropdownModule,
        InputNumberModule,
        ButtonModule,
        ToastModule
      ],
      providers: [MessageService]
    }).compileComponents();

    fixture = TestBed.createComponent(InvoiceItemFormComponent);
    component = fixture.componentInstance;
    component.invoiceId = 1;
    component.products = mockProducts;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});