import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductListPageComponent } from './product-list-page.component';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ProductListComponent } from '../../components/product-list/product-list.component';
import { Product } from '../../../../core/models/product.model';

class MockRouter {
  navigate = jasmine.createSpy('navigate');
}

class MockMessageService {
  add = jasmine.createSpy('add');
}

describe('ProductListPageComponent', () => {
  let component: ProductListPageComponent;
  let fixture: ComponentFixture<ProductListPageComponent>;
  let router: Router;
  let messageService: MessageService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductListPageComponent, ProductListComponent],
      providers: [
        { provide: Router, useClass: MockRouter },
        { provide: MessageService, useClass: MockMessageService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductListPageComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    messageService = TestBed.inject(MessageService);
    fixture.detectChanges();
  });
});