import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductListComponent } from './product-list.component';
import { ProductService } from '../../../../core/services/api/product.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { of, throwError } from 'rxjs';
import { Product } from '../../../../core/models/product.model';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { TagModule } from 'primeng/tag';

describe('ProductListComponent', () => {
  let component: ProductListComponent;
  let fixture: ComponentFixture<ProductListComponent>;
  let productService: jasmine.SpyObj<ProductService>;
  let confirmationService: jasmine.SpyObj<ConfirmationService>;
  let messageService: jasmine.SpyObj<MessageService>;

  const mockProducts: Product[] = [
    {
      id: 1,
      productCode: 'PROD-001',
      description: 'Produto Teste 1',
      productStatus: 'ACTIVE'
    },
    {
      id: 2,
      productCode: 'PROD-002',
      description: 'Produto Teste 2',
      productStatus: 'INACTIVE'
    }
  ];

  beforeEach(async () => {
    const productServiceSpy = jasmine.createSpyObj('ProductService', ['getAllProducts', 'deleteProduct']);
    const confirmationServiceSpy = jasmine.createSpyObj('ConfirmationService', ['confirm']);
    const messageServiceSpy = jasmine.createSpyObj('MessageService', ['add']);

    await TestBed.configureTestingModule({
      imports: [
        ProductListComponent,
        TableModule,
        ButtonModule,
        ConfirmDialogModule,
        ToastModule,
        TagModule
      ],
      providers: [
        { provide: ProductService, useValue: productServiceSpy },
        { provide: ConfirmationService, useValue: confirmationServiceSpy },
        { provide: MessageService, useValue: messageServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductListComponent);
    component = fixture.componentInstance;
    productService = TestBed.inject(ProductService) as jasmine.SpyObj<ProductService>;
    confirmationService = TestBed.inject(ConfirmationService) as jasmine.SpyObj<ConfirmationService>;
    messageService = TestBed.inject(MessageService) as jasmine.SpyObj<MessageService>;
    
    productService.getAllProducts.and.returnValue(of(mockProducts));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with correct status severity mapping', () => {
    expect(component.getSeverity('ACTIVE')).toBe('success');
    expect(component.getSeverity('INACTIVE')).toBe('danger');
    expect(component.getSeverity('PENDING')).toBe('warn');
  });

  it('should load products on init', () => {
    expect(productService.getAllProducts).toHaveBeenCalled();
    expect(component.products).toEqual(mockProducts);
    expect(component.loading).toBeFalse();
  });

  it('should handle error when loading products', () => {
    productService.getAllProducts.and.returnValue(throwError(() => new Error('Error')));
    component.loadProducts();
    expect(messageService.add).toHaveBeenCalledWith(jasmine.objectContaining({
      severity: 'error',
      detail: 'Falha ao carregar produtos'
    }));
    expect(component.loading).toBeFalse();
  });

  it('should emit edit event', () => {
    spyOn(component.editProduct, 'emit');
    component.onEdit(mockProducts[0]);
    expect(component.editProduct.emit).toHaveBeenCalledWith(mockProducts[0]);
  });

  it('should show confirmation when deleting', () => {
    component.onDelete(mockProducts[0]);
    expect(confirmationService.confirm).toHaveBeenCalled();
  });

  it('should delete product and show success message', () => {
    productService.deleteProduct.and.returnValue(of({}));
    component.deleteProduct(1);
    expect(productService.deleteProduct).toHaveBeenCalledWith(1);
    expect(messageService.add).toHaveBeenCalledWith(jasmine.objectContaining({
      severity: 'success',
      detail: 'Produto excluído'
    }));
  });

  it('should handle delete error', () => {
    productService.deleteProduct.and.returnValue(throwError(() => new Error('Error')));
    component.deleteProduct(1);
    expect(messageService.add).toHaveBeenCalledWith(jasmine.objectContaining({
      severity: 'error',
      detail: 'Falha ao excluir produto'
    }));
  });
});