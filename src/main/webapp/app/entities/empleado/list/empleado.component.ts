import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmpleado } from '../empleado.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { EmpleadoService } from '../service/empleado.service';
import { EmpleadoDeleteDialogComponent } from '../delete/empleado-delete-dialog.component';

@Component({
  selector: 'jhi-empleado',
  templateUrl: './empleado.component.html',
})
export class EmpleadoComponent implements OnInit {
  empleados?: IEmpleado[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  //Variables necesarias para filtrar la tabla
  filtroId?: number;
  filtroName?: string;
  filtroSurName?: string;
  filtroDni?: string;

  constructor(
    protected empleadoService: EmpleadoService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.empleadoService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IEmpleado[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  //Metodo para filtrar la tabla de forma multiple
  filter(page?: number, dontNavigate?: boolean): void {
    if (this.filtroId !== undefined || this.filtroName !== undefined || this.filtroSurName !== undefined || this.filtroDni !== undefined) {
      this.isLoading = true;
      const pageToLoad: number = page ?? this.page ?? 1;
      let idA = '';
      if (this.filtroId !== undefined) {
        idA = this.filtroId.toString();
      }
      this.empleadoService
        .filter({
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
          id: idA,
          nombre: this.filtroName !== undefined ? this.filtroName : '',
          apellidos: this.filtroSurName !== undefined ? this.filtroSurName : '',
          dni: this.filtroDni !== undefined ? this.filtroDni : '',
        })
        .subscribe(
          (res: HttpResponse<IEmpleado[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          () => {
            this.isLoading = false;
            this.onError();
          }
        );
    }
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: IEmpleado): number {
    return item.id!;
  }

  delete(empleado: IEmpleado): void {
    const modalRef = this.modalService.open(EmpleadoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.empleado = empleado;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IEmpleado[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/empleado'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.empleados = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
