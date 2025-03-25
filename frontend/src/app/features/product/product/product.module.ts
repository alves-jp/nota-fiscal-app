import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { InputNumberModule } from 'primeng/inputnumber';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ProductRoutingModule } from './product-routing.module';
import { ProductListPageComponent } from '../pages/product-list-page/product-list-page.component';
import { ProductCreatePageComponent } from '../pages/product-create-page/product-create-page.component';
import { ProductEditPageComponent } from '../pages/product-edit-page/product-edit-page.component';
import { ProductFormComponent } from '../components/product-form/product-form.component';
import { ProductListComponent } from '../components/product-list/product-list.component';


@NgModule({
  declarations: [
  
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ProductRoutingModule,
    
    ProductListPageComponent,
    ProductCreatePageComponent,
    ProductEditPageComponent,
    ProductFormComponent,
    ProductListComponent,
    
    TableModule,
    ButtonModule,
    InputTextModule,
    DialogModule,
    ToastModule,
    InputNumberModule,
    ConfirmDialogModule
  ],
  providers: [
    MessageService,
    ConfirmationService
  ]
})
export class ProductModule { }
