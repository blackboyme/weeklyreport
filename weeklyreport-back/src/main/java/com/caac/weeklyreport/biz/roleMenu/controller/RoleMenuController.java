//package com.caac.weeklyreport.biz.roleMenu.controller;
//
//import com.caac.weeklyreport.biz.roleMenu.entity.RoleMenu;
//import com.caac.weeklyreport.biz.roleMenu.service.IRoleMenuService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * <p>
// * 角色菜单关联表 前端控制器
// * </p>
// *
// * @author hanrenjie
// * @since 2025-06-05
// */
//@RestController
//@RequestMapping("/api/v1/role-menu")
//public class RoleMenuController {
//
//    private final IRoleMenuService roleMenuService;
//
//    public RoleMenuController(IRoleMenuService roleMenuService) {
//        this.roleMenuService = roleMenuService;
//    }
//
//    @PostMapping
//    public ResponseEntity<RoleMenu> createRoleMenu(@RequestBody RoleMenu roleMenu) {
//        return ResponseEntity.ok(roleMenuService.createRoleMenu(roleMenu));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<RoleMenu> getRoleMenu(@PathVariable String id) {
//        RoleMenu roleMenu = roleMenuService.getRoleMenuById(id);
//        return roleMenu != null ? ResponseEntity.ok(roleMenu) : ResponseEntity.notFound().build();
//    }
//
//    @GetMapping
//    public ResponseEntity<List<RoleMenu>> getAllRoleMenus() {
//        return ResponseEntity.ok(roleMenuService.getAllRoleMenus());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<RoleMenu> updateRoleMenu(@PathVariable String id, @RequestBody RoleMenu roleMenu) {
//        roleMenu.setRoleMenuId(id);
//        return ResponseEntity.ok(roleMenuService.updateRoleMenu(roleMenu));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteRoleMenu(@PathVariable String id) {
//        roleMenuService.deleteRoleMenu(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/role/{roleId}")
//    public ResponseEntity<List<RoleMenu>> getRoleMenusByRoleId(@PathVariable String roleId) {
//        return ResponseEntity.ok(roleMenuService.getRoleMenusByRoleId(roleId));
//    }
//
//    @GetMapping("/menu/{menuId}")
//    public ResponseEntity<List<RoleMenu>> getRoleMenusByMenuId(@PathVariable String menuId) {
//        return ResponseEntity.ok(roleMenuService.getRoleMenusByMenuId(menuId));
//    }
//
//    @PostMapping("/batch")
//    public ResponseEntity<Void> batchAssignMenusToRole(@RequestParam String roleId, @RequestBody List<String> menuIds) {
//        roleMenuService.batchAssignMenusToRole(roleId, menuIds);
//        return ResponseEntity.ok().build();
//    }
//}
