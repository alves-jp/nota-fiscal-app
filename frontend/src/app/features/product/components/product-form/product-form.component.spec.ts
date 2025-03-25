import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductFormComponent } from './product-form.component';
import { MessageService } from 'primeng/api';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { SelectButtonModule } from 'primeng/selectbutton';
import { ButtonModule } from 'primeng/button';

describe('ProductFormComponent', () => {
  let component: ProductFormComponent;
  let fixture: ComponentFixture<ProductFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        FormsModule,
        ReactiveFormsModule,
        ToastModule,
        InputTextModule,
        SelectButtonModule,
        ButtonModule
      ],
      providers: [MessageService]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with default values', () => {
    expect(component.productForm.value).toEqual({
      id: null,
      code: '',
      name: '',
      status: 'ACTIVE'
    });
  });

  it('should validate required fields', () => {
    const form = component.productForm;
    expect(form.valid).toBeFalse();
    
    form.patchValue({
      code: 'TEST123',
      name: 'Test Product'
    });
    expect(form.valid).toBeTrue();
  });
});