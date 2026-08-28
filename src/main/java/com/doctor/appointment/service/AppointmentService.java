package com.doctor.appointment.service;

import com.doctor.appointment.dto.AppointmentRequest;
import com.doctor.appointment.dto.AppointmentResponse;
import com.doctor.appointment.dto.DailyAppointmentsResponse;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {


    // اضافه کردن نوبت‌های جدید (با تقسیم به بازه‌های ۳۰ دقیقه‌ای)
    List<AppointmentResponse> addAppointments(AppointmentRequest request);

    // مشاهده همه نوبت‌های یک روز (باز و رزرو شده)
    DailyAppointmentsResponse getDailyAppointments(LocalDate date);

    // مشاهده نوبت‌های باز
    List<AppointmentResponse> getOpenAppointments(LocalDate date);

    // مشاهده نوبت‌های رزرو شده
    List<AppointmentResponse> getTakenAppointments(LocalDate date);

    // مشاهده همه نوبت‌ها (بدون فیلتر تاریخ)
    List<AppointmentResponse> getAllAppointments();

    // حذف نوبت باز
    void deleteOpenAppointment(Long appointmentId);

}
