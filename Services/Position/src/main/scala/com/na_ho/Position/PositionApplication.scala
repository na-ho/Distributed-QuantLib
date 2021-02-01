package com.na_ho.Position

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
class PositionApplication

object PositionApplication {
  def main(args: Array[String]): Unit ={
    SpringApplication.run(classOf[PositionApplication], args: _*)
  }

}