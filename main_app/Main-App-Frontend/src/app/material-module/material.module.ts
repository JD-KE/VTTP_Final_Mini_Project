import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {MatRadioModule} from '@angular/material/radio';
import {MatCardModule} from '@angular/material/card';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatMenuModule} from '@angular/material/menu';
import {MatTableModule} from '@angular/material/table';
import { MatNativeDateModule } from '@angular/material/core';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatDialogModule} from '@angular/material/dialog';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { MatMomentDatetimeModule } from '@mat-datetimepicker/moment';
import { MatDatetimepickerModule } from '@mat-datetimepicker/core';
import { MatMomentDateModule } from '@angular/material-moment-adapter';
import {ClipboardModule} from '@angular/cdk/clipboard';
import {MatSnackBarModule} from '@angular/material/snack-bar';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    MatDatepickerModule,
    MatMenuModule,
    MatTableModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatDialogModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    MatMomentDatetimeModule,
    MatDatetimepickerModule,
    MatMomentDateModule,
    ClipboardModule,
    MatSnackBarModule
  ],
  exports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    MatDatepickerModule,
    MatMenuModule,
    MatTableModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatDialogModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    MatMomentDatetimeModule,
    MatDatetimepickerModule,
    MatMomentDateModule,
    ClipboardModule,
    MatSnackBarModule
  ]
})
export class MaterialModule { }